package org.saur.initilaiser;

import org.saur.model.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.saur.Constants.BLANK_SPACE;
import static org.saur.Constants.REGEX;

public class TreeParser {

    public static Map<String, Node> parseTree(List<String> paths) {
        Map<String, Node> tree = new HashMap<String, Node>();
        for (String path : paths) {
            String[] separatedPaths = path.replaceFirst(REGEX, BLANK_SPACE).split(REGEX);
            Node previousNode = null;
            String temp = BLANK_SPACE;
            for (String separatedPath : separatedPaths) {
                temp = temp + REGEX + separatedPath;
                if (!tree.containsKey(temp)) {
                    Node currentNode = new Node(REGEX + separatedPath);
                    if (tree.containsKey(temp.replace(currentNode.getPathName(), BLANK_SPACE))) {
                        previousNode = tree.get(temp.replace(currentNode.getPathName(), BLANK_SPACE));
                    }
                    if (previousNode != null) {
                        previousNode.addNode(currentNode, temp);
                    }
                    currentNode.setPath(temp);
                    currentNode.setParent(previousNode);
                    tree.put(temp, currentNode);
                }
            }
        }
        return tree;
    }
}
